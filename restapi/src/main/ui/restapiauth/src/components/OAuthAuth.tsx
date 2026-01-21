import React, {useEffect, useState} from "react";
import {AuthPayload, AuthType} from "./AuthPage";
import {getCredentials} from "../api";

const OAuthAuth = ({onAuthChange}: { onAuthChange: (authPayload: AuthPayload) => void }) => {
    const [authUrlOAuth, setAuthUrlOAuth] = useState("");
    const [clientId, setClientId] = useState("");
    const [clientSecret, setClientSecret] = useState("");
    const [scope, setScope] = useState("");
    const [state, setState] = useState("");
    const [authUrl, setAuthUrl] = useState("");
    const [accessTokenUrl, setAccessTokenUrl] = useState("");
    const [savedCred, setSavedCred] = useState<boolean>(false);

    useEffect(() => {
        getAllCred().catch(error => console.log(error));
    }, []);

    useEffect(() => {
        onAuthChange({
            authType: AuthType.OAuth,
            authUrl: authUrlOAuth,
            clientId: clientId,
            clientSecret: clientSecret,
            scope: scope,
            state: state,
            apiUrl: authUrl,
            accessTokenUrl: accessTokenUrl,
            isAllRequiredFieldsHaveValue: areFieldsValid(),
            isCredentialsSaved: isCredSaved()
        });
    }, [authUrlOAuth, clientId, clientSecret, scope, state, authUrl, accessTokenUrl, onAuthChange]);

    const areFieldsValid = () => {
        return !!authUrlOAuth && !!authUrl && !!clientId && !!clientSecret && !!accessTokenUrl;
    };

     const isCredSaved = (): boolean =>{
                return savedCred;
     }

    const getAllCred = async () => {
        try {
            const credentials = await getCredentials(AuthType.OAuth);
            if (credentials) {
                setAuthUrlOAuth(credentials["Auth Url"]);
                setClientId(credentials["Client Id"]);
                setClientSecret(credentials["Client Secret"]);
                setScope(credentials["Scope"]);
                setState(credentials["State"]);
                setAuthUrl(credentials["Api Url"]);
                setAccessTokenUrl(credentials["Access Token Url"]);
                setSavedCred(true);
            } else {
                console.error('Failed to fetch credentials');
            }
        } catch (error) {
            console.error('Error fetching credentials:', error);
        }
    }

    return (
        <div>
            <div className="auth-form">
                <div className="form-group">
                    <label htmlFor="auth-url">Auth URL<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        id="auth-url"
                        value={authUrlOAuth}
                        onChange={(e) => setAuthUrlOAuth(e.target.value)}
                        placeholder="Enter Auth URL"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="access-token-url">Access Token URL<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        id="access-token-url"
                        value={accessTokenUrl}
                        onChange={(e) => setAccessTokenUrl(e.target.value)}
                        placeholder="Enter Access Token URL"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="client-id">Client ID<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        id="client-id"
                        value={clientId}
                        onChange={(e) => setClientId(e.target.value)}
                        placeholder="Enter Client ID"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="client-secret">Client Secret<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="password"
                        id="client-secret"
                        value={clientSecret}
                        onChange={(e) => setClientSecret(e.target.value)}
                        placeholder="Enter Client Secret"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="scope">Scope</label>
                    <input
                        type="text"
                        id="scope"
                        value={scope}
                        onChange={(e) => setScope(e.target.value)}
                        placeholder="Enter Scope (Optional)"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="state">State</label>
                    <input
                        type="text"
                        id="state"
                        value={state}
                        onChange={(e) => setState(e.target.value)}
                        placeholder="Enter State (Optional)"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="auth-verification-url">API URL<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        id="auth-verification-url"
                        value={authUrl}
                        onChange={(e) => setAuthUrl(e.target.value)}
                        placeholder="Enter API URL"
                    />
                </div>
            </div>
        </div>
    );
};

export default OAuthAuth;
