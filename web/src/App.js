import React, { useState } from 'react';
import axios from 'axios'; // Import axios for making HTTP requests

function App() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.get(`http://localhost:4567/login/${username}/${password}`);
      const data = response.data;

      if (data.status === 'ok') {
        // Login successful, perform further actions (e.g., redirect)
        console.log('Login successful');
      } else {
        // Login failed, display error message
        setError(data.message);
      }
    } catch (error) {
      // Handle error (e.g., network error)
      setError('An error occurred. Please try again later.');
    }
  };

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {error && <p>{error}</p>}
    </div>
  );
}

export default App;
