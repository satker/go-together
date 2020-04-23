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
import TextField from "@material-ui/core/TextField";

const FormLogin = ({formId, postLogin}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = () => {
        postLogin(login, password);
    };

    const handleChange = (set) => (evt) => {
        set(evt.target.value);
    };

    return <Container formId={FORM_ID}>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <TextField
                id="standard-multiline-flexible"
                label="Login"
                multiline
                rowsMax={4}
                value={login}
                onChange={handleChange(setLogin)}
            />
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <TextField
                id="standard-multiline-flexible"
                label="Password"
                multiline
                rowsMax={4}
                value={password}
                onChange={handleChange(setPassword)}
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