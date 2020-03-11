import React from "react";
import {Col, Container, Row} from "reactstrap";
import {Image} from "react-bootstrap";
import {getSrcForImg} from "../../../utils";
import {SimpleUser} from "../../../../types";

const SimpleUserView = ({item}) => {
    return <Container>
        <Row>
            <Col xs={6} md={4}>
                <Image src={getSrcForImg(item.userPhoto)} roundedCircle/>
            </Col>
            <Col xs={6} md={4}>
                <Row>{item.login}</Row>
                <Row>{item.firstName}, {item.lastName}</Row>
            </Col>
        </Row>
    </Container>
};

SimpleUserView.propTypes = {
    item: SimpleUser.isRequired
};

export default SimpleUserView;