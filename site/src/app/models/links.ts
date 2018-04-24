import {Graph, GraphImpl} from './graph';

export interface Links {
    cfg: Graph;
    mpg: string;
    driver: string;
    filename: string;
    instance_id: string;
    length: string;
    offset: string;
    pcg: Graph;
    status: string;
    title: string;
    type: string;
}
export class LinksImpl implements Links {

    cfg: Graph;
    mpg: string;
    driver: string;
    filename: string;
    instance_id: string;
    length: string;
    offset: string;
    pcg: Graph;
    status: string;
    title: string;
    type: string;

    constructor(object: any) {
        this.driver = object.driver;
        this.mpg = object.mpg;
        this.cfg = new GraphImpl(object.cfg);
        this.pcg = new GraphImpl(object.pcg);
        this.filename = object.filename;
        this.instance_id = object.instance_id;
        this.length = object.length;
        this.offset = object.offset;
        this.pcg = object.pcg;
        this.status = object.status;
        this.title = object.title;
        this.type = object.type;
    }
}
