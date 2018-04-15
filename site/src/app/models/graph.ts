export interface Graph {
    link: string[];
}


export class GraphImpl implements Graph {

    link: string[] = [];

    constructor(object: any) {
        Object.values(object).forEach(data => {
           this.link.push(data);
        });
    }
}
