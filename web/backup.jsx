// Old header
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AppBar, Box, Button, Modal, TextField, Toolbar, Typography } from '@mui/material';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};

function Header() {
  const [openModal, setOpenModal] = useState(false);
  const [titleInput, setTitleInput] = useState('');
  const [userInput, setUserInput] = useState('');
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    fetchMessages();
  }, []);

  const fetchMessages = async () => {
    try {
      const response = await fetch('https://team-goku.dokku.cse.lehigh.edu/messages');
      if (!response.ok) {
        throw new Error('Failed to fetch messages');
      }
      const data = await response.json();
      setMessages(data.mData);
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch('https://team-goku.dokku.cse.lehigh.edu/messages', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          mUsername: 'test',
          mTitle: titleInput,
          mContent: userInput,
        }),
      });
      if (!response.ok) {
        throw new Error('Failed to post message');
      }
      const data = await response.json();
      console.log('Response from server:', data);
      setTitleInput('');
      setUserInput('');
      handleCloseModal();
      fetchMessages();
      window.location.reload(); // Reload the page
    } catch (error) {
      console.error('Error posting message:', error);
    }
  };

  return (
    <div>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static" style={{ background: '#2E3B55' }}>
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              The Buzz
            </Typography>
            <Button color="inherit">Login</Button>
            <Button color="inherit" component={Link} to ="/signup">Sign Up</Button>
            <Button color="inherit" onClick={handleOpenModal}>Messages</Button>
            <Button color="inherit" component={Link} to="/profile">Profile</Button>
          </Toolbar>
        </AppBar>
      </Box>
      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="modal-messages-title"
        aria-describedby="modal-messages-description"
      >
        <Box sx={style}>
          <Typography id="modal-messages-title" variant="h6" component="h2">
            Add Messages
          </Typography>
          <TextField
            id="title-input"
            label="Title"
            variant="outlined"
            value={titleInput}
            onChange={(e) => setTitleInput(e.target.value)}
            sx={{ mt: 2 }}
          />
          <TextField
            id="user-input"
            label="Type your message"
            variant="outlined"
            value={userInput}
            onChange={(e) => setUserInput(e.target.value)}
            sx={{ mt: 2 }}
          />
          <Button onClick={handleSubmit} variant="contained" color="primary" sx={{ mt: 2 }}>
            Post
          </Button>
        </Box>
      </Modal>
    </div>
  );
}

export default Header;
