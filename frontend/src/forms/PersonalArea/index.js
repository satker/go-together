import React, {useEffect} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import LoadableContent from "forms/utils/components/LoadableContent";
import {connect} from "App/Context";

import {getCheckMail, getUserInfo, updatedUser} from "./actions";
import ViewForm from "./ViewForm";

const PersonalArea = ({userInfo, getUserInfo, updatedUser, putUpdatedUser}) => {
    useEffect(() => {
        getUserInfo();
    }, [getUserInfo]);
    return (
        <Container>
            <LoadableContent loadableData={userInfo}>
                <ItemContainer>
                    <h3>Welcome, {userInfo.response.firstName} {userInfo.response.lastName}</h3>
                </ItemContainer>
                <ViewForm profile={userInfo.response}/>
            </LoadableContent>
        </Container>
    );
};

const mapStateToProps = state => ({
    userInfo: state.components.forms.personalArea.userInfo,
    updatedUser: state.components.forms.personalArea.updatedUser,
});

export default connect(mapStateToProps, {getUserInfo, putUpdatedUser: updatedUser, getCheckMail})(PersonalArea);