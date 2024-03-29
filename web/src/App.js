// dont touch this file
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Header />} />
                {/* Add more routes as needed */}
            </Routes>
        </Router>
    );
};

export default App;
