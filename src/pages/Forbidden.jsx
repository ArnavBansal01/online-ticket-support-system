import Header from "../components/Header";
import Footer from "../components/Footer";

export default function Forbidden() {
  return (
    <>
      <Header />
      <div className="section">
        <h2>403 - Forbidden</h2>
        <p>You do not have access to this page.</p>
      </div>
      <Footer />
    </>
  );
}
