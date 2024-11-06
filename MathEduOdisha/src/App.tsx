import React from "react";
import AppRouter from "./router/AppRouter";
import NavigationBar from "./components/Navbar";
import Footer from "./components/Footer";

const App: React.FC = () => {
  return (
    <>
      <AppRouter />
      <Footer />
    </>
  );
};

export default App;
