import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { googleSignUp } from "../../services/authServices";

function GoogleOAuth() {
  const handleSuccess = (response) => {
    console.log('Login Success:', response);
    googleSignUp(response);
  };
  const handleError = (error) => {
    console.log('Login Failed:', error);
  };
  return (
    <GoogleOAuthProvider clientId="992406545501-gsf4drs652b27m7886oq1ttlfrq3o14t.apps.googleusercontent.com">
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          margin: '20px',
        }}
      >
        <GoogleLogin
          onSuccess={handleSuccess}
          onError={handleError}
          theme="outline"
          className="googleButton"
          text="Sign Up with Google"
        />
      </div>
    </GoogleOAuthProvider>
  );
}

export default GoogleOAuth;
