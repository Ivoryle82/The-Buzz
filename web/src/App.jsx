// dont touch this file
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Messages from './pages/Messages'
import Profile from './pages/Profile'; // Import the Profile component

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Messages />} />
                <Route path="/profile" element={<Profile />} />
                {/* Add more routes as needed */}
            </Routes>
        </Router>
    );
};

export default App;
