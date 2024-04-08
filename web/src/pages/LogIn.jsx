import React, { useState } from 'react';
import Header from '../components/Header';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async () => {
    try {
      const response = await fetch(`http://localhost:4567/login/${username}/${password}`);
      if (response.ok) {
        // Login successful
        const responseData = await response.json();
        // Redirect or perform other actions based on successful login
      } else {
        // Login failed
        setError('Failed to login. Please check your username and password.');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('An error occurred while logging in.');
    }
  };

  return (
    <div>
      <Header />
      
    </div>
  );
}

export default Login;
