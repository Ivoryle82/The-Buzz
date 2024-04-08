import React from 'react';
import Profile from './Profile'; // Assuming Profile.jsx is in the same directory

function UserProfilePage() {
  // Sample user data
  const user = {
    name: 'Ivory Le',
    email: 'hhl226@lehigh.edu',
    sexualIdentity: 'Female',
    genderOrientation: 'Straight',
    note: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'
  };

  return (
    <div>
      <h1>User Profile</h1>
      {/* Render the Profile component and pass the user prop */}
      <Profile user={user} />
    </div>
  );
}

export default UserProfilePage;
