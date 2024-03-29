import React, { useState } from 'react';

function Signup() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [bio, setBio] = useState('');
  const [email, setEmail] = useState('');

  const handleSignup = async () => {
    try {
      const response = await fetch(`http://localhost:4567/signup/${username}/${password}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ mBio: bio, mEmail: email }),
      });
      if (response.ok) {
        // Signup successful, handle success (e.g., redirect to dashboard)
        window.location.href = '/dashboard';
      } else {
        // Signup failed, handle error
        const errorData = await response.json();
        console.error('Signup failed:', errorData.message);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
  
  return (
    <div>
      <h2>Sign Up</h2>
      <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
      <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
      <input type="text" placeholder="Bio" value={bio} onChange={(e) => setBio(e.target.value)} />
      <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
      <button onClick={handleSignup}>Sign Up</button>
    </div>
  );
}

export default Signup;
