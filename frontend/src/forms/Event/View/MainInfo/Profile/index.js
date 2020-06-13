import React from "react";
import {Card, CardContent, CardMedia, Typography} from "@material-ui/core";

import {getSrcForImg} from "forms/utils/utils";
import {User} from "forms/utils/types";
import RightContainer from "forms/utils/components/Container/RightContainer";
import {connect} from "App/Context";

const Profile = ({author}) => {
    return <RightContainer>
        <Card style={{align: 'center', width: '350px'}}>
            {author.userPhotos ?
                <CardMedia style={{height: '255px'}}
                           component="img"
                           image={getSrcForImg(author.userPhotos[0])}/> : null}
            <CardContent>
                <Typography component="h6" variant="h6">
                    {author.firstName + ' ' + author.lastName}
                </Typography>
                <Typography component="h6" variant="h6">
                    {author.location.name + ' ' + author.location.country.name}
                </Typography>
                <Typography href={"#"}>{author.mail}</Typography>
                <Typography>My interests: {author.interests.map(interest => interest.name).join(', ')}</Typography>
                <Typography>Languages: {author.languages.map(lang => lang.name).join(', ')}</Typography>
                <Typography>About owner: {author.description}</Typography>
            </CardContent>
        </Card>
    </RightContainer>;
};

Profile.props = {
    author: User.isRequired
};

const mapStateToProps = (state) => ({
    author: state.components.forms.event.eventView.event.response.author
});

export default connect(mapStateToProps, null)(Profile);