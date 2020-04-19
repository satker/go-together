import React, {useState} from 'react';
import '../../Form.css'
import {connect} from "../../App/Context";
import MenuItem from "@material-ui/core/MenuItem";
import {Button} from "reactstrap";
import Input from "reactstrap/es/Input";
import Label from "reactstrap/es/Label";
import * as PropTypes from "prop-types";
import './style.css'
import {FORM_ID} from "./constants";
import {postLogin} from "./actions";

const FormLogin = ({formId, postLogin}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = () => {
        postLogin(login, password);
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
    formId: PropTypes.string.isRequired,
    postLogin: PropTypes.func.isRequired
};

export default connect(null, {postLogin})(FormLogin)(FORM_ID);