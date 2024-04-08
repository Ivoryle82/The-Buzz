import React, { useEffect } from 'react';
import Header from '../components/Header';

function Signup() {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  const handleSignIn = () => {
    // Redirect the user to Google OAuth URL
    window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth?' +
      'response_type=code&' +
      'client_id=233303483347-q0bt1d0gt235ji0k0nna3ilufa6d35qr.apps.googleusercontent.com&' +
      'scope=openid%20email%20profile&' +
      'redirect_uri=http://localhost:4567/oauth-redirect&' +
      'nonce=0394852-3190485-2490358';
  };

  return (
    <div style={styles.container}>
      <Header />
      <div
        id="g_id_onload"
        data-client_id="929506237505-qjdjlu4u3fnkahvt78j1faum6fjfrkqv.apps.googleusercontent.com"
        data-context="signup"
        data-ux_mode="popup"
        data-login_uri="http://localhost:3000"
        data-nonce=""
        data-auto_prompt="false"
        style={styles.googleButtonContainer}
      ></div>

      <div
        className="g_id_signin"
        data-type="standard"
        data-shape="pill"
        data-theme="filled_blue"
        data-text="Sign in with Google"
        data-size="large"
        data-logo_alignment="left"
        data-width="400"
        style={styles.googleButton}
        onClick={handleSignIn}
      ></div>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: '100vh',
  },
  googleButtonContainer: {
    marginBottom: '20px',
  },
  googleButton: {
    cursor: 'pointer',
    borderRadius: '5px',
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)',
    padding: '10px 20px',
    backgroundColor: '#4285F4',
    color: '#FFF',
    fontSize: '16px',
  },
};

export default Signup;
