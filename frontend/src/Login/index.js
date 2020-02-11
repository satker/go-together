import React, {useContext, useState} from 'react';
import '../Form.css'
import {Context} from "../Context";
import {LOGIN_URL} from '../utils/constants'
import {loginFetch} from "../utils/api/request";
import MenuItem from "@material-ui/core/MenuItem";
import {Button} from "reactstrap";
import Input from "reactstrap/es/Input";
import Label from "reactstrap/es/Label";
import * as PropTypes from "prop-types";

const FormLogin = ({formId}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [state, setState] = useContext(Context);

    const handleSubmit = () => {
        loginFetch(LOGIN_URL, {username: login, password: password}, state,
            setState);
    };

    const handleChange = (set) => (evt) => {
        set(evt.target.value);
    };

    return <>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <Label>Login</Label>
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <div className='login-input'>
                <Input type="text"
                       id={formId + "loginInput"}
                       name="login"
                       onChange={handleChange(setLogin)}/>
            </div>
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <Label>Password</Label>
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <div className='login-input'>
                <Input type="password"
                       id={formId + "passwordInput"}
                       name="password"
                       onChange={handleChange(setPassword)}/>
            </div>
        </MenuItem>
        <MenuItem button={true}
                  component={formId + 'login'}>
            <Button className="btn btn-success"
                    onClick={handleSubmit}>Login</Button>
        </MenuItem>
    </>;
};

FormLogin.propTypes = {
    formId: PropTypes.string.isRequired
};

export default FormLogin;