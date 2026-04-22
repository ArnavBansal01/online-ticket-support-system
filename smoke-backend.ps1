$ErrorActionPreference = 'Stop'

function Get-StatusCodeFromException($ex) {
  if ($ex.Exception.Response -and $ex.Exception.Response.StatusCode) {
    return [int]$ex.Exception.Response.StatusCode
  }
  return -1
}

$results = New-Object System.Collections.Generic.List[Object]
function Add-Result($name, $ok, $detail) {
  $results.Add([pscustomobject]@{ Check = $name; Pass = $ok; Detail = $detail })
}

$adminLogin = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/auth/login' -ContentType 'application/json' -Body '{"email":"admin@test.com","password":"admin123"}'
$agentLogin = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/auth/login' -ContentType 'application/json' -Body '{"email":"agent@test.com","password":"agent123"}'
$userLogin = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/auth/login' -ContentType 'application/json' -Body '{"email":"user@test.com","password":"user123"}'

$adminToken = $adminLogin.token
$agentToken = $agentLogin.token
$userToken = $userLogin.token
$userId = $userLogin.user.id

Add-Result 'Login admin/agent/customer' (($adminToken -ne $null) -and ($agentToken -ne $null) -and ($userToken -ne $null)) "adminId=$($adminLogin.user.id), agentId=$($agentLogin.user.id), userId=$userId"

$me = Invoke-RestMethod -Method Get -Uri 'http://localhost:8081/api/auth/me' -Headers @{ Authorization = "Bearer $userToken" }
Add-Result '/api/auth/me with user token' ($me.id -eq $userId) "me.id=$($me.id), role=$($me.role)"

$usersAdmin = Invoke-RestMethod -Method Get -Uri 'http://localhost:8081/api/users' -Headers @{ Authorization = "Bearer $adminToken" }
$usersAdminCount = @($usersAdmin).Count
Add-Result 'Admin can list users' ($usersAdminCount -ge 3) "count=$usersAdminCount"

try {
  Invoke-RestMethod -Method Get -Uri 'http://localhost:8081/api/users' -Headers @{ Authorization = "Bearer $userToken" } | Out-Null
  Add-Result 'Customer forbidden on /api/users' $false 'Expected 403 but got success'
} catch {
  $code = Get-StatusCodeFromException $_
  Add-Result 'Customer forbidden on /api/users' ($code -eq 403) "status=$code"
}

$ticketList = Invoke-RestMethod -Method Get -Uri 'http://localhost:8082/api/tickets?page=0&size=10' -Headers @{ Authorization = "Bearer $userToken" }
Add-Result 'Customer list own tickets' ($ticketList.items -ne $null) "returned=$(@($ticketList.items).Count)"

$newTicket = Invoke-RestMethod -Method Post -Uri 'http://localhost:8082/api/tickets' -Headers @{ Authorization = "Bearer $userToken" } -ContentType 'application/json' -Body '{"title":"Smoke E2E Auth Ticket","description":"This ticket is created in automated smoke testing to validate lifecycle close flow.","priority":"HIGH","category":"TECHNICAL"}'
$ticketId = $newTicket.id
Add-Result 'Customer create ticket' ($ticketId -ne $null) "ticketId=$ticketId"

$anonTicket = Invoke-RestMethod -Method Post -Uri 'http://localhost:8082/api/tickets' -ContentType 'application/json' -Body '{"title":"Contact Form Inquiry","description":"Name: Test User\nEmail: test@example.com\nSubject: Help\nMessage: Please contact me regarding account issue.","priority":"LOW","category":"GENERAL"}'
Add-Result 'Anonymous contact-style ticket create' ($anonTicket.id -ne $null) "ticketId=$($anonTicket.id), createdBy=$($anonTicket.createdBy)"

$assignRes = Invoke-RestMethod -Method Put -Uri ("http://localhost:8082/api/tickets/{0}/assign" -f $ticketId) -Headers @{ Authorization = "Bearer $adminToken" } -ContentType 'application/json' -Body '{"agentId":2}'
Add-Result 'Admin assign ticket' ($assignRes.assignedTo -eq 2) "assignedTo=$($assignRes.assignedTo)"

$statusRes = Invoke-RestMethod -Method Put -Uri ("http://localhost:8082/api/tickets/{0}/status" -f $ticketId) -Headers @{ Authorization = "Bearer $agentToken" } -ContentType 'application/json' -Body '{"status":"RESOLVED"}'
Add-Result 'Agent mark resolved' ($statusRes.status -eq 'RESOLVED') "status=$($statusRes.status)"

$closeRes = Invoke-RestMethod -Method Put -Uri ("http://localhost:8082/api/tickets/{0}/close" -f $ticketId) -Headers @{ Authorization = "Bearer $userToken" }
$hasClosedAt = ($null -ne $closeRes.closedAt) -and ("$($closeRes.closedAt)".Length -gt 0)
Add-Result 'Customer close resolved own ticket' (($closeRes.status -eq 'CLOSED') -and $hasClosedAt) "status=$($closeRes.status), closedAt=$($closeRes.closedAt)"

$summary = Invoke-RestMethod -Method Get -Uri 'http://localhost:8083/api/analytics/summary' -Headers @{ Authorization = "Bearer $adminToken" }
Add-Result 'Analytics summary (admin)' ($summary.totalTickets -ge 1) "total=$($summary.totalTickets), open=$($summary.openCount)"

$agentPerf = Invoke-RestMethod -Method Get -Uri 'http://localhost:8083/api/analytics/agent-performance' -Headers @{ Authorization = "Bearer $adminToken" }
$sample = @($agentPerf) | Select-Object -First 1
$sampleName = if ($null -ne $sample) { $sample.agentName } else { '' }
Add-Result 'Agent performance includes names' (-not [string]::IsNullOrWhiteSpace($sampleName)) "sampleAgentName=$sampleName"

try {
  Invoke-RestMethod -Method Get -Uri 'http://localhost:8083/api/analytics/agent-performance' -Headers @{ Authorization = "Bearer $userToken" } | Out-Null
  Add-Result 'Customer forbidden on agent-performance' $false 'Expected 403 but got success'
} catch {
  $code = Get-StatusCodeFromException $_
  Add-Result 'Customer forbidden on agent-performance' ($code -eq 403) "status=$code"
}

$customerStats = Invoke-RestMethod -Method Get -Uri ("http://localhost:8083/api/analytics/customer-stats/{0}" -f $userId) -Headers @{ Authorization = "Bearer $userToken" }
Add-Result 'Customer stats endpoint works' ($customerStats.totalTickets -ge 1) "totalTickets=$($customerStats.totalTickets)"

$results | Format-Table -AutoSize
$failed = @($results | Where-Object { -not $_.Pass })
Write-Output "FAILED_COUNT=$($failed.Count)"
if ($failed.Count -gt 0) {
  Write-Output 'FAILED_CHECKS:'
  $failed | Select-Object Check,Detail | Format-Table -AutoSize
}
