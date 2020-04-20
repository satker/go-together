import React from 'react';
import {Event} from "../../../utils/types";
import PropTypes from "prop-types";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import LeftContainer from "../../../utils/components/Container/LeftContainer";
import RightContainer from "../../../utils/components/Container/RightContainer";
import CommonInfo from './CommonInfo';
import Photos from "./Photos";

const MainInfo = ({event, onChangeEvent}) => {
    return <ContainerColumn>
        <LeftContainer style={{width: '600px'}}>
            <CommonInfo event={event} onChangeEvent={onChangeEvent}/>
        </LeftContainer>
        <RightContainer style={{width: '600px'}}>
            <Photos onChangeEvent={onChangeEvent} event={event}/>
        </RightContainer>
    </ContainerColumn>;
};

MainInfo.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default MainInfo;