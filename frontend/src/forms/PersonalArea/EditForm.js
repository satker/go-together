import React, {useEffect, useState} from "react";
import ItemContainer from "../utils/components/Container/ItemContainer";
import Container from "../utils/components/Container/ContainerRow";
import {getCheckMail} from "./actions";
import {connect} from "../../App/Context";
import PropTypes from "prop-types";
import LabeledInput from "../utils/components/LabeledInput";
import CustomButton from "../utils/components/CustomButton";

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

    const handleMail = (value) => {
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

    const handleName = (value, type) => {
        if (value === '') {
            if (type === 'firstName') {
                setIsFirstNameReadyForRegister(true);
                return;
            } else {
                setIsLastNameReadyForRegister(true);
                return;
            }
        }
        let regexToCheckNonNumericValue = new RegExp(PATTERN_TO_CHECK_NAME);
        let isGood = regexToCheckNonNumericValue.test(value);
        if (type === 'firstName') {
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
            <LabeledInput
                id="firstName"
                label="First name"
                value={profile.firstName}
                onChange={(value) => {
                    onChange('firstName', value);
                    handleName(value, 'firstName');
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                id="lastName"
                label="Last name"
                value={profile.lastName}
                onChange={(value) => {
                    onChange('lastName', value);
                    handleName(value, 'lastName');
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                id="mail"
                label="Mail address"
                value={profile.mail}
                onChange={(value) => {
                    onChange('mail', value);
                    handleMail(value);
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <CustomButton color='primary'
                          disabled={!(isMailReadyForRegister &&
                              isFirstNameReadyForRegister &&
                              isLastNameReadyForRegister)}
                          onClick={onSubmit} text='Save changes'/>
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

const mapStateToProps = () => (state) => ({
    checkMail: state.components.forms.personalArea.checkMail
});

export default connect(mapStateToProps, {getCheckMail})(EditForm);