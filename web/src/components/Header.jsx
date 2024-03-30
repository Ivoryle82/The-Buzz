import React, { useState, useEffect } from 'react';
import { AppBar, Box, Button, Modal, TextField, Toolbar, Typography } from '@mui/material';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';

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

function App() {
  const [openModal, setOpenModal] = useState(false);
  const [editModalOpen, setEditModal] = useState(false);
  const [titleInput, setTitleInput] = useState('');
  const [userInput, setUserInput] = useState('');
  const [editIndex, setEditIndex] = useState(null);
  const [messages, setMessages] = useState([]);
  const [liked, setLiked] = useState([]);

  useEffect(() => {
    fetchMessages();
  }, []);

  useEffect(() => {
    setLiked(Array(messages.length).fill(false));
  }, [messages]);

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

  const handleTitleChange = (event) => {
    setTitleInput(event.target.value);
  };

  const handleInputChange = (event) => {
    setUserInput(event.target.value);
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
    } catch (error) {
      console.error('Error posting message:', error);
    }
  };

  const handleLikeToggle = async (index) => {
    try {
      const message = messages[index];
      if (message) {
        const { mMessageID, mUsername } = message;
        const response = await fetch(`https://team-goku.dokku.cse.lehigh.edu/messages/${mMessageID}/like`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            mUsername: mUsername,
          }),
        });
        if (!response.ok) {
          throw new Error('Failed to toggle like');
        }
        const data = await response.json();
        console.log('Response from server:', data);
      } else {
        console.error('Message not found');
      }
    } catch (error) {
      console.error('Error toggling like:', error);
    }
  };

  const handleDelete = async (index) => {
    try {
      const message = messages[index];
      if (message) {
        const { mMessageID, mUsername } = message;
        const response = await fetch(`https://team-goku.dokku.cse.lehigh.edu/messages/${mMessageID}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            mUsername: mUsername,
          }),
        });
        if (!response.ok) {
          throw new Error('Failed to delete message');
        }
        const data = await response.json();
        console.log('Response from server:', data);
        const updatedMessages = messages.filter((msg, idx) => idx !== index);
        setMessages(updatedMessages);
      } else {
        console.error('Message not found');
      }
    } catch (error) {
      console.error('Error deleting message:', error);
    }
  };

  const handleEdit = async (index) => {
    try {
      const message = messages[index];
      console.log(message);
      if (message) {
        const { mMessageID, mUsername } = message; // Extracting necessary properties from the message object
  
        const response = await fetch(`https://team-goku.dokku.cse.lehigh.edu/messages/${mMessageID}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            mUsername: mUsername,
            mTitle: titleInput,
            mContent: userInput,
          }),
        });
  
        if (!response.ok) {
          throw new Error('Failed to update message');
        }
  
        const data = await response.json();
        console.log('Response from server:', data);
  
        // Clear input fields and close the modal after successful update
        setTitleInput('');
        setUserInput('');
        setEditIndex(null);
        setEditModal(false);
  
        // Fetch updated messages
        fetchMessages();
      } else {
        console.error(`Message at index ${index} does not exist`);
      }
    } catch (error) {
      console.error('Error updating message:', error);
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
            <Button color="inherit">Sign Up</Button>
            <Button color="inherit" onClick={handleOpenModal}>Messages</Button>
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
            onChange={handleTitleChange}
            sx={{ mt: 2 }}
          />
          <TextField
            id="user-input"
            label="Type your message"
            variant="outlined"
            value={userInput}
            onChange={handleInputChange}
            sx={{ mt: 2 }}
          />
          <Button onClick={handleSubmit} variant="contained" color="primary" sx={{ mt: 2 }}>
            Post
          </Button>
        </Box>
      </Modal>
      <Modal
        open={editModalOpen}
        onClose={handleCloseModal}
        aria-labelledby="modal-messages-title"
        aria-describedby="modal-messages-description"
      >
        <Box sx={style}>
          <Typography id="modal-messages-title" variant="h6" component="h2">
            Edit Message
          </Typography>
          <TextField
            id="title-input"
            label="Title"
            variant="outlined"
            value={titleInput}
            onChange={handleTitleChange}
            sx={{ mt: 2, mb: 2, display: 'block' }}
          />
          <TextField
            id="user-input"
            label="Type your message"
            variant="outlined"
            value={userInput}
            onChange={handleInputChange}
            sx={{ mt: 2, mb: 2, display: 'block' }}
          />
          <Button onClick={() => { handleEdit(editIndex); setEditModal(false); }} variant="contained" color="primary" sx={{ mt: 2 }}>
            Update
          </Button>
        </Box>
      </Modal>
      <Box sx={{ mt: 4, px: 4 }}>
        <Typography variant="h4" component="h1">Messages</Typography>
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell>Messages</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {messages.map((message, index) => (
                <TableRow key={message.mId} sx={{ fontSize: '0.8rem' }}>
                  <TableCell className={index % 2 === 0 ? 'message-left message-cell' : 'message-right message-cell'} style={{ whiteSpace: 'pre-line', transition: 'background-color 0.3s' }}>
                    {message.mTitle}<br />{message.mContent}
                  </TableCell>
                  <TableCell align="center" sx={{ padding: '2px', transition: 'background-color 0.3s' }}>
                    <IconButton aria-label="like" onClick={() => handleLikeToggle(index)}>
                        <FavoriteIcon />
                    </IconButton>
                    <IconButton aria-label="edit" onClick={() => { setEditIndex(index); setEditModal(true); }}>
                      <EditIcon />
                    </IconButton>
                    <IconButton aria-label="delete" onClick={() => handleDelete(index)}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </div>
  );
}

export default App;
