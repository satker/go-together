import React, {useEffect} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import LoadableContent from "forms/utils/components/LoadableContent";
import {connect} from "App/Context";

import {getAllInterests, getAllLanguages, getUserInfo, URL_USER} from "./actions";
import TextField from "forms/utils/components/Form/fields/TextField";
import AutocompleteLocationField from "forms/utils/components/Form/fields/AutocompleteLocationField";
import SelectBoxLoadableField from "forms/utils/components/Form/fields/SelectBoxLoadableField";
import PhotoField from "forms/utils/components/Form/fields/PhotoField";
import {
    compareFieldsValidation,
    isEmptyArrayValidation,
    isEmptyValidation,
    lengthValidation,
    regexValidation,
    validatePhoto
} from "forms/utils/validation";
import {PATTERN_TO_CHECK_NAME} from "forms/Register/constants";
import {createReduxForm} from "forms/utils/components/Form";
import {FORM_ID} from "./constants";
import {showNotification} from "forms/utils/components/Notification/actions";

const PersonalArea = ({
                          userInfo, getUserInfo, getAllLanguages, getAllInterests,
                          allLanguages, allInterests, showNotification
                      }) => {
    useEffect(() => {
        getAllInterests();
    }, [getAllInterests]);

    useEffect(() => {
        getAllLanguages();
    }, [getAllLanguages]);

    useEffect(() => {
        getUserInfo();
    }, [getUserInfo]);

    return (
        <Container>
            <LoadableContent loadableData={userInfo}>
                <RegisterForm onSubmitOk={() => showNotification("Refreshed personal data successfully!")}
                              defaultValue={userInfo.response}>
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
            </LoadableContent>
        </Container>
    );
};

const mapStateToProps = state => ({
    userInfo: state.components.forms.personalArea.userInfo,
    updatedUser: state.components.forms.personalArea.updatedUser.response,
    allLanguages: state.components.forms.personalArea.allLanguages,
    allInterests: state.components.forms.personalArea.allInterests
});

const validation = (fields) => {
    return {
        ...regexValidation(fields, ['firstName', 'lastName'], PATTERN_TO_CHECK_NAME),
        ...lengthValidation(fields, ['description'], 255),
        ...compareFieldsValidation(fields, 'password', 'confirmPassword'),
        ...validatePhoto(fields, 'groupPhoto.photos'),
        ...isEmptyValidation(fields,
            ['firstName', 'lastName', 'description', 'location']),
        ...isEmptyArrayValidation(fields, ['groupPhoto.photos', 'languages', 'interests'])
    }
}

const RegisterForm = createReduxForm({FORM_ID, validation, url: URL_USER + '/users'});

export default connect(mapStateToProps, {
    getUserInfo,
    getAllLanguages,
    getAllInterests,
    showNotification
})(PersonalArea);