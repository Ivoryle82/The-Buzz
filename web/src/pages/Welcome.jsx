import React from 'react';
import './Welcome.css'; // Import CSS file for styling
import Header from '../components/Header';

function WelcomePage() {

    const handleSignIn = () => {
        const googleAuthURL = 'https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=233303483347-q0bt1d0gt235ji0k0nna3ilufa6d35qr.apps.googleusercontent.com&scope=openid%20email%20profile&redirect_uri=https://team-goku.dokku.cse.lehigh.edu/oauth-redirect&nonce=0394852-3190485-2490358&';
        window.location.href = googleAuthURL;
    };

    return (
        <div className="welcome-container">
            <Header />
            <div className="background-image-1"></div>
            <div className="background-image-2"></div>
            <div className="background-image-3"></div>
            <div className="background-image-4"></div>
            <div className="foreground-content">
                <h1 className="slogan">All your <span>messages</span> in one place</h1>
                <button className="google-login-button" onClick={handleSignIn}>
                    Sign in through Google
                </button>
            </div>
        </div>
    );
}

export default WelcomePage;
