import React, {useState} from 'react';
import '../../Form.css'
import {connect} from "../../App/Context";
import MenuItem from "@material-ui/core/MenuItem";
import * as PropTypes from "prop-types";
import './style.css'
import {FORM_ID} from "./constants";
import {postLogin} from "./actions";
import Container from "../utils/components/Container/ContainerRow";
import Button from "@material-ui/core/Button";
import LabeledInput from "../utils/LabeledInput";

const FormLogin = ({formId, postLogin}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = () => {
        postLogin(login, password);
    };

    return <Container formId={FORM_ID}>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <LabeledInput
                id="login"
                label="Login"
                value={login}
                onChange={setLogin}
            />
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <LabeledInput
                type='password'
                id="password"
                label="Password"
                value={password}
                onChange={setPassword}
            />
        </MenuItem>
        <MenuItem button={true}
                  component={formId + 'login'}>
            <Button onClick={handleSubmit}>Login</Button>
        </MenuItem>
    </Container>;
};

FormLogin.propTypes = {
    formId: PropTypes.string.isRequired,
    postLogin: PropTypes.func.isRequired
};

export default connect(null, {postLogin})(FormLogin)(FORM_ID);