import React, {useContext, useEffect, useState} from "react";
import {Button, Container, Form, Input, Table} from "reactstrap";
import '../Form.css'
import {Context} from "../Context";
import {USER_SERVICE_URL} from '../utils/constants'

const URL_USER = USER_SERVICE_URL + "/users/_id_";
const PATTERN_TO_CHECK_MAIL = '(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])';
const PATTERN_TO_CHECK_NAME = '^[A-Za-z]*$';


const PersonalArea = () => {
    const [state] = useContext(Context);
    const [isEdited, setIsEdited] = useState(false);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [mail, setMail] = useState('');
    const [login, setLogin] = useState('');
    const [isMailReadyForRegister, setIsMailReadyForRegister] = useState(true);
    const [isFirstNameReadyForRegister, setIsFirstNameReadyForRegister] = useState(true);
    const [isLastNameReadyForRegister, setIsLastNameReadyForRegister] = useState(true);

    useEffect(() => {
        state.fetchWithToken(URL_USER.replace('_id_', state.userId), resp => {
            setFirstName(resp.firstName);
            setLastName(resp.lastName);
            setMail(resp.mail);
            setLogin(resp.login);
        })
    }, [setFirstName, setLastName, setMail, state]);

    const onSubmit = async (evt) => {
        let error = 'Check your new';
        if (!isMailReadyForRegister) {
            error = error + ' mail ';
        }
        if (!isFirstNameReadyForRegister) {
            error = error + ' first name ';
        }
        if (!isLastNameReadyForRegister) {
            error = error + ' last name';
        }
        if (error !== 'Check your new') {
            alert(error);
            return;
        }

        let body = {
            login: login
        };
        for (let key of ["firstName", "lastName", "mail"]) {
            if ([key] === '') {
                body[key] = state.user[key];
            } else {
                if (key === 'firstName') body[key] = firstName;
                if (key === 'lastName') body[key] = lastName;
                if (key === 'mail') body[key] = mail;
            }
        }

        state.fetchWithToken(URL_USER.replace('_id_', state.userId), resp => {
            setMail(resp.mail);
            setFirstName(resp.firstName);
            setLastName(resp.lastName)
        }, 'PUT', body);
    };

    const onChange = (set) => (evt) => {
        set(evt.target.value);
    };

    const handleMail = async (evt) => {
        let value = evt.target.value;
        if (value === '') {
            setIsMailReadyForRegister(true);
            return;
        }
        let regexChecker = new RegExp(PATTERN_TO_CHECK_MAIL);
        if (!regexChecker.test(value)) {
            setIsMailReadyForRegister(false);
            return;
        }
        const URLtoCheck = USER_SERVICE_URL + '/mail/check/_mail_';
        state.fetchWithToken(URLtoCheck.replace("_mail_", value), resp => {
            if (resp !== true) {
                setIsMailReadyForRegister(true);
            }
        });
    };

    const handleName = (evt) => {
        let value = evt.target.value;
        if (evt.target.value === '') {
            if (evt.target.name === 'firstName') {
                setIsFirstNameReadyForRegister(true);
                return;
            } else {
                setIsLastNameReadyForRegister(true);
                return;
            }
        }
        let regexToCheckNonNumericValue = new RegExp(PATTERN_TO_CHECK_NAME);
        let isGood = regexToCheckNonNumericValue.test(value);
        if (evt.target.name === 'firstName') {
            isGood ? setIsFirstNameReadyForRegister(true) :
                setIsFirstNameReadyForRegister(false);
        } else {
            isGood ? setIsLastNameReadyForRegister(true) :
                setIsLastNameReadyForRegister(false);
        }
    };

    return (
        <Container>
            <b><h3>Welcome, {firstName}</h3></b>
            <Table>
                <tbody>
                <tr>
                    <th scope="row">Login</th>
                    <td>{login}</td>
                </tr>
                <tr>
                    <th scope="row">Mail</th>
                    <td>{mail}</td>
                </tr>
                <tr>
                    <th scope="row">First name</th>
                    <td>{firstName}</td>
                </tr>
                <tr>
                    <th scope="row">Last name</th>
                    <td>{lastName}</td>
                </tr>
                </tbody>
            </Table>
            <br/>
            <br/>
            <Button className="btn-danger" onClick={() => setIsEdited(true)}>Edit profile</Button>
            {isEdited ?
                <Form className="wide-form" onSubmit={onSubmit}>
                    <p><b>Edit your profile</b></p>
                    <Table>
                        <tbody>
                        <tr>
                            <td>First name</td>
                            <td><input
                                className={!isFirstNameReadyForRegister ? "error" : "good"}
                                type="text" name="firstName" placeholder={firstName}
                                onChange={(evt) => {
                                    onChange(setFirstName)(evt);
                                    handleName(evt);
                                }}/></td>
                        </tr>
                        <tr>
                            <td>Last name</td>
                            <td><input
                                className={!isLastNameReadyForRegister ? "error" : "good"}
                                type="text" name="lastName" placeholder={lastName}
                                onChange={(evt) => {
                                    onChange(setLastName)(evt);
                                    handleName(evt);
                                }}/>
                            </td>
                        </tr>
                        <tr>
                            <td>Mail address</td>
                            <td><input
                                className={!isMailReadyForRegister ? "error" : "good"}
                                type="text" name="mail" placeholder={mail}
                                onChange={(evt) => {
                                    onChange(setMail)(evt);
                                    handleMail(evt);
                                }}/></td>
                        </tr>
                        </tbody>
                    </Table>
                    <Input className="btn btn-success form-control" disabled={!(isMailReadyForRegister &&
                        isFirstNameReadyForRegister &&
                        isLastNameReadyForRegister)} type="submit" value="Save changes"/>
                </Form>
                : null
            }
        </Container>
    );
};

export default PersonalArea;