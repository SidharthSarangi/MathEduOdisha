import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Book from "../pages/Book";
import Event from "../pages/Event";
import Magazines from "../pages/Magazines";
import Trust from "../pages/Trust";
// import Navbar from '../components/Navbar';
import NavigationBar from "../components/Navbar";
import Home from "../pages/Home";
import Authors from "../pages/Authors";
import LoginPage from "../pages/Login";
import SignupPage from "../pages/SignUp";
import NotFound from "../pages/NotFound";
import AboutUs from "../pages/AboutUs";
import PublicationPage from "../pages/Publication";

const AppRouter: React.FC = () => {
  return (
    <Router>
      <NavigationBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about-us" element={<AboutUs />} />
        <Route path="/publication" element={<PublicationPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/books" element={<Book />} />
        <Route path="/magazines" element={<Magazines />} />
        <Route path="/events" element={<Event />} />
        <Route path="/trust" element={<Trust />} />
        <Route path="/author" element={<Authors />} />
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
