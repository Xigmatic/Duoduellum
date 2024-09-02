export function getRGBFromColor(colorName) {
    switch(colorName) {
        case "red":
            return [255, 85, 85];
            
        case "orange":
            return [255, 170, 0];
            
        case "yellow":
            return [255, 255, 85];
            
        case "lime":
            return [85, 255, 85];
            
        case "green":
            return [0, 170, 0];
            
        case "light_blue":
            return [85, 255, 255];
            
        case "cyan":
            return [0, 170, 170];
            
        case "blue":
            return [85, 85, 255];
            
        case "purple":
            return [170, 0, 170];
            
        case "pink":
            return [255, 85, 255];
            
        case "white":
            return [255, 255, 255];
            
        case "gray":
            return [85, 85, 85];
            
        case "black":
            return [0, 0, 0];
            
        default:
            return [255,255,255];
            
    }
}