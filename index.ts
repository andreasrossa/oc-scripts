export * from './lib/async';
export * from './lib/number';

console.time('a');

type Matrix2D = number[][];
type NodeGraph = Map<number, Node>;
interface Pos2D {
  x: number;
  z: number;
}

interface Node {
  pos: Pos2D;
  index: number;
  neighbours: number[];
}

const matrix: Matrix2D = [
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
];

function comparePos(a: Pos2D, b: Pos2D): boolean {
  return a.x == b.x && a.z == b.z;
}

function posHash(pos: Pos2D, sX: number): number {
  return pos.z + 1 * sX - pos.x;
}

function getNeighbours(currentPos: Pos2D, graph: NodeGraph): number[] {
  return graph
    .filter((n) => {
      console.log(currentPos, '->', n.pos);
      return (
        comparePos({ x: currentPos.x, z: currentPos.z - 1 }, n.pos) ||
        comparePos({ x: currentPos.x, z: currentPos.z + 1 }, n.pos) ||
        comparePos({ x: currentPos.x - 1, z: currentPos.z }, n.pos) ||
        comparePos({ x: currentPos.x + 1, z: currentPos.z }, n.pos)
      );
    })
    .map((n) => n.index);
}

const graph: NodeGraph = [];

for (let z = 0; z < matrix.length; z++) {
  for (let x = 0; x < matrix[z].length; x++) {
    const tile = matrix[z][x];
    if (tile != 0) continue; // Continue if not air-block

    const n = getNeighbours({ x: x, z: z }, graph);

    graph.push({
      index: graph.length,
      pos: { x: x, z: z },
      neighbours: n,
    });
  }
}

graph.forEach((e) =>
  e.neighbours.forEach((n) => {
    const g = graph.find((i) => i.index == n).neighbours;
    if (!g.includes(e.index)) g.push(e.index);
  })
);

console.table(graph);
console.timeEnd('a');
