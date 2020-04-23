import React from "react";
import {getSrcForImg} from "../../../../utils/utils";
import {User} from "../../../../utils/types";
import RightContainer from "../../../../utils/components/Container/RightContainer";
import {Card, CardContent, CardMedia, Typography} from "@material-ui/core";

const Profile = ({user}) => {
    return <RightContainer style={{width: '400px'}}>
        <Card style={{align: 'center'}}>
            {user.userPhotos ?
                <CardMedia style={{height: '255px'}}
                           component="img"
                           image={getSrcForImg(user.userPhotos[0])}/> : null}
            <CardContent>
                <Typography component="h6" variant="h6">
                    {user.firstName + ' ' + user.lastName}
                </Typography>
                <Typography component="h6" variant="h6">
                    {user.location.name + ' ' + user.location.country.name}
                </Typography>
                <Typography href={"#"}>{user.mail}</Typography>
                <Typography>My interests: {user.interests.map(interest => interest.name).join(', ')}</Typography>
                <Typography>Languages: {user.languages.map(lang => lang.name).join(', ')}</Typography>
                <Typography>About owner: {user.description}</Typography>
            </CardContent>
        </Card>
    </RightContainer>;
};

Profile.props = {
    user: User.isRequired
};

export default Profile;