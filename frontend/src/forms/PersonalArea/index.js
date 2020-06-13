import React, {useEffect, useState} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import {onChange} from "forms/utils/utils";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import LoadableContent from "forms/utils/components/LoadableContent";
import CustomButton from "forms/utils/components/CustomButton";
import {connect} from "App/Context";

import {getCheckMail, getUserInfo, putUpdatedUser} from "./actions";
import EditForm from "./EditForm";
import ViewForm from "./ViewForm";

const PersonalArea = ({userInfo, getUserInfo, updatedUser, putUpdatedUser}) => {
    const [isEdited, setIsEdited] = useState(false);
    const [profile, setProfile] = useState({
        firstName: '',
        lastName: '',
        mail: '',
        login: ''
    });

    useEffect(() => {
        getUserInfo();
    }, [getUserInfo]);

    useEffect(() => {
        if (userInfo) {
            const newProfile = {
                firstName: userInfo.response.firstName,
                lastName: userInfo.response.lastName,
                mail: userInfo.response.mail,
                login: userInfo.response.login
            };

            setProfile(newProfile)
        }
    }, [setProfile, userInfo]);

    useEffect(() => {
        if (updatedUser) {
            const newProfile = {
                firstName: updatedUser.firstName,
                lastName: updatedUser.lastName,
                mail: updatedUser.mail,
                login: updatedUser.login
            };

            setProfile(newProfile)
        }
    }, [setProfile, updatedUser]);

    const onSubmit = () => {
        putUpdatedUser(profile);
    };

    return (
        <Container>
            <ItemContainer>
                <h3>Welcome, {profile.firstName}</h3>
            </ItemContainer>
            <LoadableContent loadableData={userInfo}>
                <ViewForm profile={profile}/>
            </LoadableContent>
            <ItemContainer>
                <CustomButton text='Edit profile'
                              onClick={() => setIsEdited(true)}/>
            </ItemContainer>
            {isEdited && <EditForm onChange={onChange(profile, setProfile)}
                                   onSubmit={onSubmit}
                                   profile={profile}/>}
        </Container>
    );
};

const mapStateToProps = state => ({
    userInfo: state.components.forms.personalArea.userInfo,
    updatedUser: state.components.forms.personalArea.updatedUser,
});

export default connect(mapStateToProps, {getUserInfo, putUpdatedUser, getCheckMail})(PersonalArea);