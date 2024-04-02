import React, { useState, useEffect } from 'react';
import Header from '../components/Header'
import { useParams } from 'react-router-dom';

const Profile = () => {
  // Use useParams to get the userId from the URL
  const { userId } = useParams();

  // State to manage user data
  const [userData, setUserData] = useState({
    name: '',
    email: '',
    sexualIdentity: '',
    genderOrientation: '',
    note: ''
  });

  // useEffect to fetch user data when component mounts
  useEffect(() => {
    // Make API call to fetch user data based on userId
    // Example fetch call, replace with actual API call
    fetch(`/api/users/${userId}`)
      .then(response => response.json())
      .then(data => {
        setUserData(data); // Set user data received from API response
      })
      .catch(error => {
        console.error('Error fetching user data:', error);
      });
  }, [userId]); // useEffect runs whenever userId changes

  // Function to handle changes in input fields
  const handleChange = (event) => {
    const { name, value } = event.target;
    setUserData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  // Function to save changes made to the profile
  const saveChanges = () => {
    // Make API call to update user data
    // Example fetch call, replace with actual API call
    fetch(`/api/users/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData)
    })
    .then(response => {
      if (response.ok) {
        console.log('User data updated successfully');
      } else {
        console.error('Failed to update user data');
      }
    })
    .catch(error => {
      console.error('Error updating user data:', error);
    });
  };

  return (
    <div>
        <Header/>
      <h2>User Profile</h2>
      <div>
        <label>Name:</label>
        <input type="text" name="name" value={userData.name} onChange={handleChange} />
      </div>
      <div>
        <label>Email:</label>
        <input type="email" name="email" value={userData.email} onChange={handleChange} />
      </div>
      <div>
        <label>Sexual Identity:</label>
        <input type="text" name="sexualIdentity" value={userData.sexualIdentity} onChange={handleChange} />
      </div>
      <div>
        <label>Gender Orientation:</label>
        <input type="text" name="genderOrientation" value={userData.genderOrientation} onChange={handleChange} />
      </div>
      <div>
        <label>Note:</label>
        <textarea name="note" value={userData.note} onChange={handleChange}></textarea>
      </div>
      <button onClick={saveChanges}>Save Changes</button>
    </div>
  );
};

export default Profile;
