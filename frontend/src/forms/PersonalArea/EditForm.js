import React, {useEffect, useState} from "react";
import ItemContainer from "../utils/components/Container/ItemContainer";
import {Input} from "reactstrap";
import Container from "../utils/components/Container/ContainerRow";
import {FORM_ID} from "./constants";
import {getCheckMail} from "./actions";
import {connect} from "../../App/Context";
import PropTypes from "prop-types";

const PATTERN_TO_CHECK_MAIL = '(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])';
const PATTERN_TO_CHECK_NAME = '^[A-Za-z]*$';

const EditForm = ({checkMail, getCheckMail, onChange, onSubmit, profile}) => {
    const [isMailReadyForRegister, setIsMailReadyForRegister] = useState(true);
    const [isFirstNameReadyForRegister, setIsFirstNameReadyForRegister] = useState(true);
    const [isLastNameReadyForRegister, setIsLastNameReadyForRegister] = useState(true);

    useEffect(() => {
        if (checkMail) {
            if (checkMail !== true) {
                setIsMailReadyForRegister(true);
            }
        }
    }, [setIsMailReadyForRegister, checkMail]);

    const handleMail = (evt) => {
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

    return <Container>
        <ItemContainer>Edit your profile</ItemContainer>
        <ItemContainer>
            First name: <input
            className={!isFirstNameReadyForRegister ? "error" : "good"}
            type="text" name="firstName" placeholder={profile.firstName}
            onChange={(evt) => {
                onChange('firstName', evt);
                handleName(evt);
            }}/>
        </ItemContainer>
        <ItemContainer>
            Last name: <input
            className={!isLastNameReadyForRegister ? "error" : "good"}
            type="text" name="lastName" placeholder={profile.lastName}
            onChange={(evt) => {
                onChange('lastName', evt);
                handleName(evt);
            }}/>
        </ItemContainer>
        <ItemContainer>
            Mail address: <input
            className={!isMailReadyForRegister ? "error" : "good"}
            type="text" name="mail" placeholder={profile.mail}
            onChange={(evt) => {
                onChange('mail', evt);
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
};

EditForm.propTypes = {
    checkMail: PropTypes.bool,
    getCheckMail: PropTypes.func.isRequired,
    onChange: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    profile: PropTypes.object
};

const mapStateToProps = (FORM_ID) => (state) => ({
    checkMail: state[FORM_ID]?.checkMail
});

export default connect(mapStateToProps, {getCheckMail})(EditForm)(FORM_ID);