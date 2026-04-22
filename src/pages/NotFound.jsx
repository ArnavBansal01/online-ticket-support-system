import Header from "../components/Header";
import Footer from "../components/Footer";

export default function NotFound() {
  return (
    <>
      <Header />
      <div className="section">
        <h2>404 - Not Found</h2>
        <p>The page you are looking for does not exist.</p>
      </div>
      <Footer />
    </>
  );
}
