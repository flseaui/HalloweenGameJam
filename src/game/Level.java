package game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static game.Shaders.textureShader;

public class Level {
	
	public Vector2f[] points;
	public Line[] lines;
	public Texture[] textures;
	public VAO[] texturePositions;
	
	private String tag = "";
	
	public Level(Vector2f[] pts) {
		points = pts;
		lines = new Line[pts.length - 1];
		for (int i = 1; i < pts.length; ++i) {
			lines[i - 1] = new Line(i - 1, i, this);
		}
	}
	public Level(Vector2f[] pts, Texture[] tex, VAO[] texPos) {
		points = pts;
		lines = new Line[pts.length - 1];
		for (int i = 1; i < pts.length; ++i) {
			lines[i - 1] = new Line(i - 1, i, this);
		}
		textures = tex;
		texturePositions = texPos;
	}
	public Level(String path) {
		Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(path));
		tag = s.nextLine();
		ArrayList<Texture> texArray = new ArrayList<>();
		ArrayList<VAO> vaoArray = new ArrayList<>();
		while (s.nextLine().equals("{")) {
			texArray.add(new Texture(s.nextLine()));
			float x = Float.parseFloat(s.nextLine());
			float y = Float.parseFloat(s.nextLine());
			float width = Float.parseFloat(s.nextLine());
			float height = Float.parseFloat(s.nextLine());
			vaoArray.add(new VAO(x, y, width, height));
			s.nextLine();
		}
		String x = s.nextLine();
		String y = s.nextLine();
		ArrayList<Vector2f> vectorArray = new ArrayList<>();
		while (!y.equals("[")) {
			vectorArray.add(new Vector2f(Float.parseFloat(x), Float.parseFloat(y)));
			x = s.nextLine();
			y = s.nextLine();
		}
		points = vectorArray.toArray(new Vector2f[0]);
		ArrayList<Line> lineArray = new ArrayList<>();
		String ind = s.nextLine();
		while (!ind.equals("]")) {
			lineArray.add(new Line(Integer.parseInt(ind), Integer.parseInt(s.nextLine()), this));
			ind = s.nextLine();
		}
		textures = texArray.toArray(new Texture[0]);
		texturePositions = vaoArray.toArray(new VAO[0]);
		lines = lineArray.toArray(new Line[0]);
	}
	
	public void render(Vector2f camPos) {
		textureShader.enable();
		Matrix4f matrix = MainView.getPerspectiveMatrix().translate(-camPos.x, -camPos.y, 0f);
		textureShader.shaders[0].uniforms[0].set(matrix);
		for (int i = 0; i < textures.length; ++i) {
			textures[i].bind();
			texturePositions[i].fullRender();
		}
	}
	
	public CollisionStats getOffsetProgress(Vector2f position, Vector2f size, Vector2f velocity) {
		float ret = 1;
		float entMinX = position.x - size.x/2;
		float entMaxX = position.x + size.x/2;
		float entMinY = position.y - size.y/2;
		float entMaxY = position.y + size.y/2;
		int collisionLine = -1;
		for (int i = 0; i < lines.length; ++i) {
			Vector2f pa = points[lines[i].inda];;
			Vector2f pb = points[lines[i].indb];
			
			float denominator = velocity.x * lines[i].delta.y - velocity.y * lines[i].delta.x;
			
			float xminLPdiff =  pa.x - entMinX;
			float yminLPdiff = pa.y - entMinY;
			float xmaxLPdiff = pa.x - entMaxX;
			float ymaxLPdiff =  pa.y - entMaxY;
			
			float a = (xminLPdiff * lines[i].delta.y - yminLPdiff * lines[i].delta.x) / denominator;
			float b = (xminLPdiff * lines[i].delta.y - ymaxLPdiff * lines[i].delta.x) / denominator;
			float c = (xmaxLPdiff * lines[i].delta.y - yminLPdiff * lines[i].delta.x) / denominator;
			float d = (xmaxLPdiff * lines[i].delta.y - ymaxLPdiff * lines[i].delta.x) / denominator;
			
			float minA;
			float maxA;
			float minB;
			float maxB;
			
			float z;
			float o;
			
			//minX
			z = xminLPdiff/velocity.x;
			o = (pb.x - entMinX)/velocity.x;
			
			if (a > b) {
				minA = b;
				maxA = a;
			} else {
				minA = a;
				maxA = b;
			}
			if (z > o) {
				minB = o;
				maxB = z;
			} else {
				minB = z;
				maxB = o;
			}
			if (maxA > minB && maxB > minA) {
				if (maxA < maxB) {
					if (maxA > 0) {
						if (minA > minB) {
							//aa
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						} else {
							//ba
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						}
					}
				} else {
					if (maxB > 0) {
						if (minB > minA) {
							//bb
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						} else {
							//ab
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						}
					}
				}
			}
			
			//minY
			z = yminLPdiff/velocity.y;
			o = (pb.y - entMinY)/velocity.y;
			if (a > c) {
				minA = c;
				maxA = a;
			} else {
				minA = a;
				maxA = c;
			}
			if (z > o) {
				minB = o;
				maxB = z;
			} else {
				minB = z;
				maxB = o;
			}
			if (maxA > minB && maxB > minA) {
				if (maxA < maxB) {
					if (maxA > 0) {
						if (minA > minB) {
							//aa
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						} else {
							//ba
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						}
					}
				} else {
					if (maxB > 0) {
						if (minB > minA) {
							//bb
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						} else {
							//ab
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						}
					}
				}
			}
			//maxX
			z = xmaxLPdiff/velocity.x;
			o = (pb.x - entMaxX)/velocity.x;
			if (d > c) {
				minA = c;
				maxA = d;
			} else {
				minA = d;
				maxA = c;
			}
			if (z > o) {
				minB = o;
				maxB = z;
			} else {
				minB = z;
				maxB = o;
			}
			if (maxA > minB && maxB > minA) {
				if (maxA < maxB) {
					if (maxA > 0) {
						if (minA > minB) {
							//aa
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						} else {
							//ba
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						}
					}
				} else {
					if (maxB > 0) {
						if (minB > minA) {
							//bb
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						} else {
							//ab
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						}
					}
				}
			}
			//maxY
			z = ymaxLPdiff/velocity.y;
			o = (pb.y - entMaxY)/velocity.y;
			if (d > b) {
				minA = b;
				maxA = d;
			} else {
				minA = d;
				maxA = b;
			}
			if (z > o) {
				minB = o;
				maxB = z;
			} else {
				minB = z;
				maxB = o;
			}
			if (maxA > minB && maxB > minA) {
				if (maxA < maxB) {
					if (maxA > 0) {
						if (minA > minB) {
							//aa
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						} else {
							//ba
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						}
					}
				} else {
					if (maxB > 0) {
						if (minB > minA) {
							//bb
							if (minB < ret) {
								ret = minB;
								collisionLine = -2;
							}
						} else {
							//ab
							if (minA < ret) {
								ret = minA;
								collisionLine = i;
							}
						}
					}
				}
			}
		}
		if (Float.isNaN(ret) || Float.isInfinite(ret)) {
			ret = 0;
		}
		if (ret < -1) {
			ret = 0;
			/*if (velocity.lengthSquared() < 0.02f) {
				ret = 0;
			}else {
				System.out.println(ret);
				ret = 1;
			}*/
		}
		return new CollisionStats(collisionLine, ret);
	}
	
	public String getTag() {
		return tag;
	}
	
	public class CollisionStats {
		public int collisionLine;
		public float collisionProportion;
		public CollisionStats(int line, float o) {
			collisionLine = line;
			collisionProportion = o;
		}
	}
}
/*
{
path
x
y
width
height
}
(
p1x
p1y
p2x
p2y
p3x
p3y
pnx
pny
)
[
l1a
l1b
l2a
l2b
l3a
l3b
lna
lnb
]
 */