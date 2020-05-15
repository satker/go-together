import React from 'react';
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import LeftContainer from "../../../utils/components/Container/LeftContainer";
import RightContainer from "../../../utils/components/Container/RightContainer";
import CommonInfo from './CommonInfo';
import Photos from "./Photos";

const MainInfo = () => {
    return <ContainerColumn>
        <LeftContainer>
            <CommonInfo/>
        </LeftContainer>
        <RightContainer>
            <Photos/>
        </RightContainer>
    </ContainerColumn>;
};

export default MainInfo;