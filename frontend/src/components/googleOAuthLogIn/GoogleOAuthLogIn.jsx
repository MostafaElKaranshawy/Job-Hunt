import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { googleSignUp, googleLogIn } from "../../services/authServices";

function GoogleOAuth({ mode }) {
  const handleSuccess = (response) => {
    if (mode === "signup") {
      googleSignUp(response); 
    } else if (mode === "login") {
      googleLogIn(response); 
    }
  };

  const handleError = (error) => {
    console.log("Login Failed:", error);
  };
  return (
    <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
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
          prompt="none"
          text={mode === "signup" ? "Sign Up with Google" : "Login with Google"}
        />
      </div>
    </GoogleOAuthProvider>
  );
}

export default GoogleOAuth;
