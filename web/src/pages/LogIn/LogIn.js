import React, { useState } from 'react';
import Header from '../../components/Header';
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
    <Header>
      
    </Header>
    /* <div>
      <h2>Login</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
      <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
      <button onClick={handleLogin}>Login</button>
    </div> */
  );
}

export default Login;
