import React, { FunctionComponent } from "react"
import { useGoogleLogin } from "react-use-googlelogin"

export const GoogleAuthContext = React.createContext({})

const GoogleAuthProvider: FunctionComponent = ({ children }) => {
  const googleAuth = useGoogleLogin({
    clientId: "12178522373-ickptrganmbe80cn2dirl89ddj4n18bl.apps.googleusercontent.com",
    persist: true,
    fetchBasicProfile: true,
    uxMode: "popup",
    redirectUri: "http://localhost:3002"
  })

  return <GoogleAuthContext.Provider value={googleAuth}>{children}</GoogleAuthContext.Provider>
}

export const useGoogleAuth = () => React.useContext(GoogleAuthContext)

export default GoogleAuthProvider
