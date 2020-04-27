import React from "react";
import ItemContainer from "../utils/components/Container/ItemContainer";
import PropTypes from "prop-types";
import ContainerRow from "../utils/components/Container/ContainerRow";

const ViewForm = ({profile}) => {
    return <ContainerRow>
        <ItemContainer>Login: {profile.login}</ItemContainer>
        <ItemContainer>Mail: {profile.mail}</ItemContainer>
        <ItemContainer>First name: {profile.firstName}</ItemContainer>
        <ItemContainer>Last name: {profile.lastName}</ItemContainer>
        <ItemContainer>Login: {profile.login}</ItemContainer>
    </ContainerRow>
};

ViewForm.propTypes = {
    profile: PropTypes.object.isRequired
};

export default ViewForm;