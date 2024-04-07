import React, { useState, useEffect } from 'react';
import { AppBar, Box, Button, Typography, Toolbar, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Modal, TextField } from '@mui/material';
import Header from '../components/Header';
import FloatingActionButtons from '../components/FloatingActionButtons';

const style = {
  position: 'fixed',
  bottom: 16,
  right: 16,
};

function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [titleInput, setTitleInput] = useState('');
  const [contentInput, setContentInput] = useState('');

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

  const handleTitleChange = (event) => {
    setTitleInput(event.target.value);
  };

  const handleContentChange = (event) => {
    setContentInput(event.target.value);
  };

  const handlePost = async () => {
    try {
      const response = await fetch('https://team-goku.dokku.cse.lehigh.edu/messages', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          mUsername: 'test',
          mTitle: titleInput,
          mContent: contentInput,
        }),
      });
      if (!response.ok) {
        throw new Error('Failed to post message');
      }
      const data = await response.json();
      console.log('Response from server:', data);
      setTitleInput('');
      setContentInput('');
      handleCloseModal();
      fetchMessages();
    } catch (error) {
      console.error('Error posting message:', error);
    }
  };

  return (
    <div>
      <Header />
      <Box sx={{ mt: 4, px: 4 }}>
        <Typography variant="h4" component="h1">Messages</Typography>
        <FloatingActionButtons onClick={handleOpenModal} />
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell>Title</TableCell>
                <TableCell>Content</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {messages.map((message) => (
                <TableRow key={message.mId}>
                  <TableCell>{message.mTitle}</TableCell>
                  <TableCell>{message.mContent}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="modal-messages-title"
        aria-describedby="modal-messages-description"
      >
        <Box sx={{ ...style, width: 400 }}>
          <Typography id="modal-messages-title" variant="h6" component="h2">
            Add Message
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
            id="content-input"
            label="Content"
            variant="outlined"
            value={contentInput}
            onChange={handleContentChange}
            sx={{ mt: 2 }}
          />
          <Button onClick={handlePost} variant="contained" color="primary" sx={{ mt: 2 }}>
            Post
          </Button>
        </Box>
      </Modal>
    </div>
  );
}

export default Dashboard;
