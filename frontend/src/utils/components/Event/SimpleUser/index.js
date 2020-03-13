import React from "react";
import {Col, Container, Row} from "reactstrap";
import {Image} from "react-bootstrap";
import {getSrcForImg} from "../../../utils";
import {SimpleUser} from "../../../../types";

const SimpleUserView = ({item}) => {
    return <Container>
        <Row>
            <Col>
                <Row><Image className='simple_user_img' src={getSrcForImg(item.userPhoto)} roundedCircle/></Row>
                <Row>{item.login}</Row>
            </Col>
            <Col>
                <Row>{item.firstName}, {item.lastName}</Row>
            </Col>
        </Row>
    </Container>
};

SimpleUserView.propTypes = {
    item: SimpleUser.isRequired
};

export default SimpleUserView;