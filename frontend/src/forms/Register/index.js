import React, {useEffect} from 'react';
import {navigate} from 'hookrouter';

import {connect} from "App/Context";

import {getAllInterests, getAllLanguages, getCheckMail, getCheckUserName, regUser} from "./actions";
import PhotoField from "forms/utils/components/Form/fields/PhotoField";
import {createReduxForm} from "forms/utils/components/Form";
import {FORM_ID, PATTERN_TO_CHECK_MAIL, PATTERN_TO_CHECK_NAME} from "./constants";
import TextField from "forms/utils/components/Form/fields/TextField";
import {
    compareFieldsValidation,
    isEmptyArrayValidation,
    isEmptyValidation,
    lengthMoreValidation,
    lengthValidation,
    regexValidation,
    validatePhoto
} from "forms/utils/validation";
import SelectBoxLoadableField from "forms/utils/components/Form/fields/SelectBoxLoadableField";
import AutocompleteLocationField from "forms/utils/components/Form/fields/AutocompleteLocationField";

const FormRegister = ({
                          allLanguages, allInterests, getAllInterests, getAllLanguages,
                          registeredUser, regUser, getCheckUserName, getCheckMail,
                          checkedMail, checkedUserName
                      }) => {

    useEffect(() => {
        getAllInterests();
    }, [getAllInterests]);

    useEffect(() => {
        getAllLanguages();
    }, [getAllLanguages]);


    useEffect(() => {
        if (registeredUser.id) {
            moveToMainPage()
        }
    }, [registeredUser]);

    const moveToMainPage = () => navigate('/');

    return <RegisterForm onClose={moveToMainPage}
                         onSubmit={regUser}>
        <TextField name='login'
                   checkValue={getCheckUserName}
                   checked={checkedUserName}
                   placeholder='Login'/>
        <TextField name='mail'
                   checkValue={getCheckMail}
                   checked={checkedMail}
                   placeholder='Mail'/>
        <TextField name='firstName'
                   placeholder='First name'/>
        <TextField name='lastName'
                   placeholder='Last name'/>
        <AutocompleteLocationField name='location'
                                   placeholder='Location'/>
        <TextField name='description'
                   placeholder='Description'/>
        <SelectBoxLoadableField name='languages'
                                options={allLanguages}
                                placeholder='Select languages'/>
        <SelectBoxLoadableField name='interests'
                                options={allInterests}
                                placeholder='Select interests'/>
        <PhotoField name='groupPhoto.photos'/>
        <TextField name='password'
                   type='password'
                   placeholder='Password'/>
        <TextField name='confirmPassword'
                   type='password'
                   placeholder='Confirm password'/>
    </RegisterForm>
};

const mapStateToProps = state => ({
    allLanguages: state.components.forms.register.allLanguages,
    allInterests: state.components.forms.register.allInterests,
    registeredUser: state.components.forms.register.registeredUser,
    checkedMail: state.components.forms.register.checkMail,
    checkedUserName: state.components.forms.register.checkUserName
});

const validation = (fields) => {
    return {
        ...regexValidation(fields, ['mail'], PATTERN_TO_CHECK_MAIL),
        ...regexValidation(fields, ['firstName', 'lastName'], PATTERN_TO_CHECK_NAME),
        ...lengthValidation(fields, ['description'], 255),
        ...lengthMoreValidation(fields, ['password', 'confirmPassword'], 8),
        ...compareFieldsValidation(fields, 'password', 'confirmPassword'),
        ...validatePhoto(fields, 'groupPhoto.photos'),
        ...isEmptyValidation(fields,
            ['login', 'mail', 'firstName', 'lastName', 'description', 'password', 'confirmPassword', 'location']),
        ...isEmptyArrayValidation(fields, ['groupPhoto.photos', 'languages', 'interests'])
    }
}

const RegisterForm = createReduxForm({FORM_ID, validation});

export default connect(mapStateToProps,
    {
        getAllLanguages,
        getAllInterests,
        regUser,
        getCheckUserName,
        getCheckMail
    })(FormRegister);
