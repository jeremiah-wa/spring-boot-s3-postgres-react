import React, {useState,useEffect,useCallback} from 'react';
import Axios from 'axios';
import {useDropzone} from 'react-dropzone'
import './App.css';

function UserProfiles() {
  const [userProfiles, setUserProfiles] = useState([]);

  function fetchUserProfiles() {
    Axios.get("http://localhost:8080/api/v1/user-profile")
    .then(res => {
      setUserProfiles(res.data);
      // console.log(res.data);
    });
  };

  useEffect(() => {
    fetchUserProfiles()
  }, []);

  return userProfiles.map((userProfile, index) => {
    return ( 
      <div key={index}>
        {userProfile.userProfileId ? (
          <img 
            src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`}
            alt="Profile Picture"
          />
        ) : null}
        <br/>
        <br/>
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <MyDropzone {...userProfile}/>
        <br/>
      </div>
    );
  });
};

function MyDropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    const file = acceptedFiles[0];
    // console.log(file);

    const formData = new FormData();
    formData.append("file", file);

    Axios.post(
      `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }
    ).then(() => {
      console.log("file uploaded successfully")
    }).catch(err => {
      console.log(err);
    })
  }, [])

  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop image files here, or click to select image</p>
      }
    </div>
  )
};

function App() {
  return (
    <div className="App">
     <UserProfiles/>
    </div>
  );
};

export default App;
