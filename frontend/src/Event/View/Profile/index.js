import React from "react";
import {Card, CardBody, CardImg, CardSubtitle, CardText, CardTitle} from "reactstrap";
import CardLink from "reactstrap/es/CardLink";
import {getSrcForImg} from "../../../utils/utils";
import {User} from "../../../types";

const Profile = ({user}) => {
    return <Card body style={{align: 'center'}}>
        {user.userPhotos ?
            <CardImg top width="50%" height="255" src={getSrcForImg(user.userPhotos[0])}
                     alt="Card image cap"/> : null}
        <CardBody>
            <CardTitle><h6>{user.firstName + ' ' + user.lastName}</h6></CardTitle>
            <CardSubtitle>
                <h7>{user.location.name + ' ' + user.location.country.name}</h7>
            </CardSubtitle>
            <CardLink href={"#"}>{user.mail}</CardLink>
            <CardText>Languages: {user.languages.map(lang => lang.name).join(', ')}</CardText>
            <CardText>About owner: {user.description}</CardText>
        </CardBody>
    </Card>;
};

Profile.props = {
    user: User.isRequired
};

export default Profile;