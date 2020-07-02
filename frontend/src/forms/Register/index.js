import React, {useEffect, useState} from 'react';
import {navigate} from 'hookrouter';

import {connect} from "App/Context";
import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import CustomReference from "forms/utils/components/CustomReference";
import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import LoadableContent from "forms/utils/components/LoadableContent";
import CustomButton from "forms/utils/components/CustomButton";

import {getAllInterests, getAllLanguages, registerUser} from "./actions";
import UserNameField from "./fields/UserNameField";
import MailField from "./fields/MailField";
import FirstNameField from "./fields/FirstNameField";
import LastNameField from "./fields/LastNameField";
import DescriptionField from "./fields/DescriptionField";
import PhotoField from "./fields/PhotoField";
import PasswordField from "./fields/PasswordField";

const FormRegister = ({
                          allLanguages, allInterests, getAllInterests, getAllLanguages,
                          registerUser, registeredUser
                      }) => {
    const [isIncorrectData, setIsIncorrectData] = useState(true);

    const [login, setLogin] = useState(null);
    const [mail, setMail] = useState(null);
    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [location, setLocation] = useState(null);
    const [description, setDescription] = useState(null);
    const [userPhoto, setUserPhoto] = useState(null);
    const [password, setPassword] = useState(null);
    const [confirmPassword, setConfirmPassword] = useState(null);
    const [languages, setLanguages] = useState([]);
    const [interests, setInterests] = useState([]);
    console.log(location)
    useEffect(() => {
        getAllInterests();
    }, [getAllInterests]);

    useEffect(() => {
        getAllLanguages();
    }, [getAllLanguages]);


    const handleSubmit = () => {
        let body = {
            login,
            mail,
            firstName,
            lastName,
            location,
            description,
            userPhotos: [userPhoto],
            password,
            languages,
            interests
        };
        registerUser(body);
    };

    useEffect(() => {
        if (registeredUser.id) {
            moveToMainPage()
        }
    }, [registeredUser]);

    const moveToMainPage = () => navigate('/');

    return <Container>
        <ItemContainer>
            <UserNameField login={login}
                           setLogin={setLogin}
                           setIsIncorrectData={setIsIncorrectData}/>
        </ItemContainer>
        <ItemContainer>
            <MailField mail={mail}
                       setMail={setMail}/>
        </ItemContainer>
        <ItemContainer>
            <FirstNameField firstName={firstName}
                            setFirstName={setFirstName}
                            setIsIncorrectData={setIsIncorrectData}/>
        </ItemContainer>
        <ItemContainer>
            <LastNameField lastName={lastName}
                           setLastName={setLastName}
                           setIsIncorrectData={setIsIncorrectData}/>
        </ItemContainer>
        <ItemContainer>
            <AutocompleteLocation onChangeLocation={setLocation}
                                  placeholder='Enter location'/>
        </ItemContainer>
        <ItemContainer>
            <DescriptionField description={description}
                              setDescription={setDescription}
                              setIsIncorrectData={setIsIncorrectData}/>
        </ItemContainer>
        <ItemContainer>
            <LoadableContent loadableData={allLanguages}>
                <MultipleSelectBox label='Select languages'
                                   value={languages}
                                   optionsSimple={allLanguages.response}
                                   onChange={setLanguages}/>
            </LoadableContent>
        </ItemContainer>
        <ItemContainer>
            <LoadableContent loadableData={allInterests}>
                <MultipleSelectBox label='Select interests'
                                   value={interests}
                                   optionsSimple={allInterests.response}
                                   onChange={setInterests}/>
            </LoadableContent>
        </ItemContainer>
        <PhotoField userPhoto={userPhoto}
                    setUserPhoto={setUserPhoto}
                    setIsIncorrectData={setIsIncorrectData}/>
        <PasswordField password={password}
                       setPassword={setPassword}
                       confirmPassword={confirmPassword}
                       setConfirmPassword={setConfirmPassword}
                       setIsIncorrectData={setIsIncorrectData}/>
        <ItemContainer>
            <CustomReference action={moveToMainPage} description='Already registered?'/>
        </ItemContainer>
        <ItemContainer>
            <CustomButton color="primary"
                          onClick={handleSubmit}
                          disabled={!isIncorrectData}
                          text='Register'/>
        </ItemContainer>
        <ItemContainer>
            <CustomButton color="secondary" onClick={moveToMainPage} text='Close'/>
        </ItemContainer>
    </Container>;
};

const mapStateToProps = state => ({
    allLanguages: state.components.forms.register.allLanguages,
    allInterests: state.components.forms.register.allInterests,
    registeredUser: state.components.forms.register.registeredUser
});

export default connect(mapStateToProps,
    {getAllLanguages, getAllInterests, registerUser})(FormRegister);
