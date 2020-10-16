import React, {useState} from "react";
import Modal from "@material-ui/core/Modal";
import makeStyles from "@material-ui/core/styles/makeStyles";

import {hideModal} from "./actions";
import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import CustomButton from "forms/utils/components/CustomButton";
import RightContainer from "forms/utils/components/Container/RightContainer";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

const rand = () => Math.round(Math.random() * 20) - 10;

const getModalStyle = () => {
    const top = 50 + rand();
    const left = 50 + rand();

    return {
        top: `${top}%`,
        left: `${left}%`,
        transform: `translate(-${top}%, -${left}%)`,
    };
}


const useStyles = makeStyles((theme) => ({
    paper: {
        position: 'absolute',
        width: 400,
        backgroundColor: theme.palette.background.paper,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        padding: theme.spacing(2, 4, 3),
    },
}));

const ModalWindow = ({modal, hideModal}) => {
    const classes = useStyles();
    const [modalStyle] = useState(getModalStyle);

    const onClick = () => {
        modal.onAction();
        hideModal();
    };

    return <Modal
        open={modal.isOpen}
        onClose={hideModal}
    >
        <div style={modalStyle} className={classes.paper}>
            <Container>
                <ItemContainer>
                    {modal.message}
                </ItemContainer>
                <ContainerColumn>
                    <LeftContainer>
                        <CustomButton color="primary" onClick={onClick} text='Ok'/>
                    </LeftContainer>
                    <RightContainer>
                        <CustomButton onClick={hideModal} text='Close'/>
                    </RightContainer>
                </ContainerColumn>
            </Container>
        </div>
    </Modal>
};

const mapStateToProps = (state) => {
    console.log(state.components.utils.modals.modal.value)
    return ({
        modal: state.components.utils.modals.modal.value
    });
}

export default connect(mapStateToProps, {hideModal})(ModalWindow);