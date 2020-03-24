import React from "react";
import {Card, CardBody, CardSubtitle, CardTitle} from "reactstrap";
import {Image} from "react-bootstrap";
import {getSrcForImg} from "../../../utils";
import {EventUser} from "../../../../types";
import DeleteButton from "../../DeleteButton/DeleteButton";
import Button from "reactstrap/es/Button";
import PropTypes from "prop-types";

const ItemSimpleUser = ({user, onDelete, onClick}) => {
    return <Card body style={{align: 'center'}}>
        <Image className='simple_user_img' src={getSrcForImg(user.user.userPhoto)} roundedCircle/>
        <CardBody>
            <CardTitle>{user.user.login}</CardTitle>
            <CardSubtitle>{user.user.firstName}, {user.user.lastName}</CardSubtitle>
        </CardBody>

        <DeleteButton onDelete={() => onDelete(user.user.id)}/>
        <Button className="btn btn-success" onClick={() => onClick(user.user.id)}>Approve user</Button>
    </Card>;
};

ItemSimpleUser.propTypes = {
    user: EventUser.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired
};

export default ItemSimpleUser;