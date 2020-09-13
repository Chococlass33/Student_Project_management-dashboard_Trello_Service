import React, { FunctionComponent } from "react"
import { useGoogleLogin } from "react-use-googlelogin"

export const GoogleAuthContext = React.createContext({})

const GoogleAuthProvider: FunctionComponent = ({ children }) => {
  const googleAuth = useGoogleLogin({
    clientId: "12178522373-qsn5v1darojtvbf0cvn8nc1dposfqqub.apps.googleusercontent.com",
    persist: true,
    fetchBasicProfile: true,
    uxMode: "redirect",
    redirectUri: process.env.REACT_APP_GOOGLE_REDIRECT_URI
  })

  return <GoogleAuthContext.Provider value={googleAuth}>{children}</GoogleAuthContext.Provider>
}

export const useGoogleAuth = () => React.useContext(GoogleAuthContext)

export default GoogleAuthProvider
