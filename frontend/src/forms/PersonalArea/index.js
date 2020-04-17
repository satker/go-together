import React, {useEffect, useState} from "react";
import {Button, Input} from "reactstrap";
import '../../Form.css'
import Container from "../utils/components/Container/ContainerRow";
import ItemContainer from "../utils/components/Container/ItemContainer";
import {getCheckMail, getUserInfo, putUpdatedUser} from "./actions";
import {connect} from "../../App/Context";
import {FORM_ID} from "./constants";

const PATTERN_TO_CHECK_MAIL = '(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])';
const PATTERN_TO_CHECK_NAME = '^[A-Za-z]*$';


const PersonalArea = ({userInfo, getUserInfo, updatedUser, putUpdatedUser, checkMail, getCheckMail}) => {
    const [isEdited, setIsEdited] = useState(false);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [mail, setMail] = useState('');
    const [login, setLogin] = useState('');
    const [isMailReadyForRegister, setIsMailReadyForRegister] = useState(true);
    const [isFirstNameReadyForRegister, setIsFirstNameReadyForRegister] = useState(true);
    const [isLastNameReadyForRegister, setIsLastNameReadyForRegister] = useState(true);

    useEffect(() => {
        getUserInfo();
    }, [getUserInfo]);

    useEffect(() => {
        if (userInfo) {
            setFirstName(userInfo.firstName);
            setLastName(userInfo.lastName);
            setMail(userInfo.mail);
            setLogin(userInfo.login);
        }
    }, [setFirstName, setLastName, setMail, setLogin, userInfo]);

    useEffect(() => {
        if (updatedUser) {
            setFirstName(updatedUser.firstName);
            setLastName(updatedUser.lastName);
            setMail(updatedUser.mail);
            setLogin(updatedUser.login);
        }
    }, [setFirstName, setLastName, setMail, setLogin, updatedUser]);

    useEffect(() => {
        if (checkMail) {
            if (checkMail !== true) {
                setIsMailReadyForRegister(true);
            }
        }
    }, [setIsMailReadyForRegister, checkMail]);

    const onSubmit = () => {
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
            login,
            firstName,
            lastName,
            mail
        };
        putUpdatedUser(body);
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

        getCheckMail(value);
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
            <ItemContainer>
                <h3>Welcome, {firstName}</h3>
            </ItemContainer>
            <ItemContainer>Login: {login}</ItemContainer>
            <ItemContainer>Mail: {mail}</ItemContainer>
            <ItemContainer>First name: {firstName}</ItemContainer>
            <ItemContainer>Last name: {lastName}</ItemContainer>
            <ItemContainer>Login: {login}</ItemContainer>
            <br/>
            <br/>
            <Button className="btn-danger" onClick={() => setIsEdited(true)}>Edit profile</Button>
            {isEdited &&
            <Container>
                <ItemContainer>Edit your profile</ItemContainer>
                <ItemContainer>
                    First name: <input
                    className={!isFirstNameReadyForRegister ? "error" : "good"}
                    type="text" name="firstName" placeholder={firstName}
                    onChange={(evt) => {
                        onChange(setFirstName)(evt);
                        handleName(evt);
                    }}/>
                </ItemContainer>
                <ItemContainer>
                    Last name: <input
                    className={!isLastNameReadyForRegister ? "error" : "good"}
                    type="text" name="lastName" placeholder={lastName}
                    onChange={(evt) => {
                        onChange(setLastName)(evt);
                        handleName(evt);
                    }}/>
                </ItemContainer>
                <ItemContainer>
                    Mail address: <input
                    className={!isMailReadyForRegister ? "error" : "good"}
                    type="text" name="mail" placeholder={mail}
                    onChange={(evt) => {
                        onChange(setMail)(evt);
                        handleMail(evt);
                    }}/>
                </ItemContainer>
                <ItemContainer>
                    <Input className="btn btn-success form-control"
                           disabled={!(isMailReadyForRegister &&
                               isFirstNameReadyForRegister &&
                               isLastNameReadyForRegister)}
                           type="submit" onClick={onSubmit}
                           value="Save changes"/>
                </ItemContainer>
            </Container>
            }
        </Container>
    );
};

const mapStateToProps = (state) => ({
    userInfo: state[FORM_ID]?.userInfo,
    updatedUser: state[FORM_ID]?.updatedUser,
    checkMail: state[FORM_ID]?.checkMail
});

export default connect(mapStateToProps, {getUserInfo, putUpdatedUser, getCheckMail}, FORM_ID)(PersonalArea);