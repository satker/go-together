import React, {useEffect, useState} from "react";
import Container from "../utils/components/Container/ContainerRow";
import {onChange} from "../utils/utils";
import ItemContainer from "../utils/components/Container/ItemContainer";
import {getCheckMail, getUserInfo, putUpdatedUser} from "./actions";
import {connect} from "../../App/Context";
import {FORM_ID} from "./constants";
import EditForm from "./EditForm";
import ViewForm from "./ViewForm";
import LoadableContent from "../utils/components/LoadableContent";
import Button from "@material-ui/core/Button";

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
        <Container formId={FORM_ID}>
            <ItemContainer>
                <h3>Welcome, {profile.firstName}</h3>
            </ItemContainer>
            <LoadableContent loadableData={userInfo}>
                <ViewForm profile={profile}/>
            </LoadableContent>
            <Button color='red' onClick={() => setIsEdited(true)}>Edit profile</Button>
            {isEdited && <EditForm onChange={onChange(profile, setProfile)}
                                   onSubmit={onSubmit}
                                   profile={profile}/>}
        </Container>
    );
};

const mapStateToProps = (FORM_ID) => (state) => ({
    userInfo: state[FORM_ID]?.userInfo,
    updatedUser: state[FORM_ID]?.updatedUser,
    checkMail: state[FORM_ID]?.checkMail
});

export default connect(mapStateToProps, {getUserInfo, putUpdatedUser, getCheckMail})(PersonalArea)(FORM_ID);