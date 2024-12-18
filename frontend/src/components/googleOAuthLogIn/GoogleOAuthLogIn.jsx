import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";
import { googleSignUp, googleLogIn } from "../../services/authServices";
import { useState } from "react";

function GoogleOAuth({ mode }) {
  const [successResponse, setSuccessResponse] = useState("");
  const [failureResponse, setFailureResponse] = useState("");

  const handleSuccess = async (response) => {
    try {
      let responseFromBack;
      if (mode === "signup") {
        responseFromBack = await googleSignUp(response);
      } else if (mode === "login") {
        responseFromBack = await googleLogIn(response);
      }

      if (responseFromBack?.success) {
        if (mode === "login") {
          setSuccessResponse(responseFromBack?.message);
          setFailureResponse("");
          window.location.href = "/home";
        } else if (mode === "signup") {
          setSuccessResponse(responseFromBack?.message);
          setFailureResponse("");
        }
      } else {
        setSuccessResponse("");
        setFailureResponse(responseFromBack?.message);
      }
    } catch (error) {
      console.error("Error during Google OAuth:", error);
      setSuccessResponse("");
      setFailureResponse("An unexpected error occurred. Please try again.");
    }
  };

  const handleError = (error) => {
    console.log("Login Failed:", error);
    setSuccessResponse("");
    setFailureResponse("Google Login Failed. Please try again.");
  };

  return (
    <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          margin: "20px",
        }}
      >
        <GoogleLogin
          onSuccess={handleSuccess}
          onError={handleError}
          theme="outline"
          className="googleButton"
          text={mode === "signup" ? "Sign Up with Google" : "Login with Google"}
        />
      </div>
      {successResponse && <p className="success-message" style={{ color: "green" }}>{successResponse}</p>}
      {failureResponse && <p className="error-message" style={{ color: "red" }}>{failureResponse}</p>}
    </GoogleOAuthProvider>
  );
}

export default GoogleOAuth;
