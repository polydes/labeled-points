import com.stencyl.behavior.Script;
import com.stencyl.graphics.fonts.BitmapFont;
import com.stencyl.graphics.G;
import com.stencyl.models.Actor;
import com.stencyl.models.actor.ActorType;
import com.stencyl.models.Font;
import com.stencyl.models.Resource;
import com.stencyl.Data;
import com.stencyl.Engine;

import openfl.geom.Point;

class PointsExtension
{
	public static var rMap:Map<Int, Map<String, Point>> = new Map<Int, Map<String, Point>>();

	public static function getPointForActor(actor:Actor, name:String):Point
	{
		return getPointForActorType(actor.type, name).add(new Point(actor.getX(), actor.getY()));
	}
	
	public static function getPointForActorType(actorType:ActorType, name:String):Point
	{
		var rid:Int = actorType.ID;
		if(!rMap.exists(rid))
			loadRid(rid);

		return rMap.get(rid).get(name);
	}
	
	public static function getPointsForActor(actor:Actor):Array<Point>
	{
		var pts = getPointsForActorType(actor.type);
		var apt = new Point(actor.getX(), actor.getY());
		return [for(p in pts) p.add(apt)];
	}

	public static function getPointsForActorType(actorType:ActorType):Array<Point>
	{
		var rid:Int = actorType.ID;
		if(!rMap.exists(rid))
			loadRid(rid);

		var points:Array<Point> = [for(p in rMap.get(rid)) p];

		return points;
	}

	public static function loopPointsForActor(actor:Actor, func:Point->Void):Void
	{
		var apt = new Point(actor.getX(), actor.getY());
		loopPointsForActorType(actor.type, function(p:Point) { func(p.add(apt)); });
	}

	public static function loopPointsForActorType(actorType:ActorType, func:Point->Void):Void
	{
		var rid:Int = actorType.ID;
		if(!rMap.exists(rid))
			loadRid(rid);

		for(p in rMap.get(rid))
			func(p);
	}

	private static function loadRid(id:Int):Void
	{
		var smap:Map<String, String> = getFileKeyValues("assets/data/com.polydes.points/" + id + ".txt");
		var map:Map<String, Point> = new Map<String, Point>();
		for(key in smap.keys())
		{
			map.set(key, rPoint(smap.get(key)));
		}
		rMap.set(id, map);
	}

	public static function rPoint(s:String):Point
	{
		var ints:Array<Int> = getInts(s);
		if(ints == null)
			return new Point(0, 0);
		
		return new Point(ints[0], ints[1]);
	}
	
	public static function getInts(s:String):Array<Int>
	{
		if(s.length == 0)
			return null;
		
		var splitString:Array<String> = s.substring(1,s.length - 1).split(",");
		var toReturn:Array<Int> = [];
		for(sub in splitString)
			toReturn.push(rInt(sub));
		return toReturn;
	}

	public static function rInt(s:String):Int
	{
		if(Math.isNaN(Std.parseFloat(s)))
			return 0;

		return Std.parseInt(s);
	}

	public static var newlinePattern:EReg = ~/[\r\n]+/g;
	
	public static function getLines(s:String):Array<String>
	{
		return newlinePattern.split(s);
	}
	
	public static function getFileLines(filename:String):Array<String>
	{
		return newlinePattern.split(nme.Assets.getText(filename));
	}

	public static function getFileKeyValues(filename:String):Map<String,String>
	{
		var lines:Array<String> = getFileLines(filename);
		var map:Map<String, String> = new Map<String, String>();
		for(line in lines)
		{
			if(line.length == 0)
				continue;
				
			var parts:Array<String> = line.split("=");
			map.set(parts[0], parts[1]);
		}
		return map;
	}
}