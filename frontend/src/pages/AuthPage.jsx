import { useState } from "react";
import { useNavigate } from "react-router-dom";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

const AuthPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const loginToAccount = async () => {
  if (username.length == 0 || password.length == 0) {
    alert("Username or password is empty.")
    return;
  }
  const options = {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify({
        username: username,
        password: password
    })
  };
  const response = await fetch(`${BACKEND_URL}/api/users/login`, options);
  const data = await response.json();
  if (response.ok) {
    localStorage.setItem("token", data.token);
    console.log(data.username + " logged in.");
  } else if (response.status === 401) {
    alert(data.error + " or create account first");
    return;
  }  
    navigate("/dashboard");
  }

  const createAccount = async () => {
    if (username.length === 0 || password.length === 0) {
      alert("Username or password is empty.")
      return;
    }
    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    };
    const response = await fetch(`${BACKEND_URL}/api/users/signup`, options);
    const data = await response.json();
    if (response.status === 400)
      alert(data.failed);
    else if(response.status === 409)
      alert(data.failed);
    else
      console.log("Account created successfully!");
  }

  return (
  <div className="flex justify-center">
      <fieldset className="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">
        <legend className="fieldset-legend pt-13">Login</legend>

        <label className="label">Username</label>
        <input 
          type="username" 
          className="input" 
          placeholder="Username" 
          value={username}
          onChange={(e) => setUsername(e.target.value)} />

        <label className="label">Password</label>
        <input 
          type="password" 
          className="input" 
          placeholder="Password" 
          value={password}
          onChange={(e) => setPassword(e.target.value)}/>

        <button className="btn btn-neutral mt-4" onClick={loginToAccount}>Login</button>
        <button className="btn btn-neutral mt-2" onClick={createAccount}>Create Account</button>
      </fieldset>
    </div>
  );
};

export default AuthPage;