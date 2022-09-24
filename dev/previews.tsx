import React from 'react';
import {ComponentPreview, Previews} from '@react-buddy/ide-toolbox';
import {PaletteTree} from './palette';
import {DefaultPageContainer} from "../components/DefaultPageContainer";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/DefaultPageContainer">
                <DefaultPageContainer/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;