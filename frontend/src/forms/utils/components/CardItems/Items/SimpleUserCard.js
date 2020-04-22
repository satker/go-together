import React from "react";
import {CardSubtitle, CardTitle} from "reactstrap";
import {getSrcForImg} from "../../../utils";
import {SimpleUser} from "../../../types";

const SimpleUserCard = ({user}) => {
    return <div className='simple-user-item form-group'>
        <div className='flex'>
            <img className='simple_user_img' src={getSrcForImg(user.userPhoto)} alt=''/>
        </div>
        <div className='flex'>
            <CardTitle>Login: {user.login}</CardTitle>
            <CardSubtitle>Name: {user.firstName}, {user.lastName}</CardSubtitle>
        </div>
    </div>;
};

SimpleUserCard.propTypes = {
    user: SimpleUser.isRequired
};

export default SimpleUserCard;